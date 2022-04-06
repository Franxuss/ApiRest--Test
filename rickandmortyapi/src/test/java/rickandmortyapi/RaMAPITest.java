package rickandmortyapi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import response.RickAndMortyResponse;
import retrofit2.Call;
import retrofit2.Response;
import service.RickAndMortyClient;
import service.RickAndMortyService;

import java.io.IOException;

public class RaMAPITest {

    private RickAndMortyService rickAndMortyService;
    private RickAndMortyClient rickAndMortyClient;
    private RickAndMortyResponse rickAndMortyResponse;
    private static final Integer charactersPerPage = 20; // Especificado en el API
    private static final Integer validPage = 19;
    private static final Integer invalidPage = 100;

    @Given("an open API connection")
    public void anOpenAPIConnection() {
        rickAndMortyClient = RickAndMortyClient.getInstance();
        rickAndMortyService = rickAndMortyClient.getRickAndMortyService();
    }

    @When("we get the characters")
    public void weGetTheCharacters() {
        //Obtenemos todos los personajes y los almacenamos en el objeto de respuesta
        Call<RickAndMortyResponse> call = rickAndMortyService.getCharacters();

        try {
            Response<RickAndMortyResponse> response = call.execute();
            if(response.isSuccessful()) { // 200: OK
                rickAndMortyResponse = response.body();
            } else { // 404: Not found
                Assert.assertEquals(200, response.code());
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Then("the number of pages is correct")
    public void theNumberOfPagesIsCorrect() {
        Integer expectedNumPages = rickAndMortyResponse.getInfo().getPages();
        Integer numCharacters = rickAndMortyResponse.getInfo().getCount();

        //Comprobamos que devuelven valores correctos
        Assert.assertNotNull(expectedNumPages);
        Assert.assertNotNull(numCharacters);
        Assert.assertTrue(expectedNumPages > 0 && numCharacters > 0);

        if(numCharacters % 20 == 0) {
            //Si el número de personajes es múltiplo de charactersPerPage, debe coincidir la división entera con el número de páginas
            Assert.assertEquals(expectedNumPages.longValue(), (numCharacters / charactersPerPage));
        } else {
            //Si no, el número de páginas debe ser una más
            Assert.assertEquals(expectedNumPages.longValue(), (numCharacters / charactersPerPage) + 1);
        }
    }

    @When("we get a certain page of characters")
    public void weGetACertainPageOfCharacters() {
        //Obtenemos todos los personajes de una determinada página y los almacenamos en el objeto de respuesta
        Call<RickAndMortyResponse> call = rickAndMortyService.getCharactersByPage(validPage);

        try {
            Response<RickAndMortyResponse> response = call.execute();
            if(response.isSuccessful()) { // 200: OK
                rickAndMortyResponse = response.body();
            } else { // 404: Not found
                Assert.assertEquals(200, response.code());
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Then("the next and previous page are correct")
    public void theNextAndPreviousPageAreCorrect() {

        Integer actualPrevPage;
        Integer actualNextPage;

        if(rickAndMortyResponse.getInfo().getPrev() == null && (validPage == 0 || validPage == 1)) { //Primera página
            //Comprobamos que la página siguiente no sea nula
            Assert.assertNotNull(rickAndMortyResponse.getInfo().getNext());

            actualNextPage = Integer.parseInt(rickAndMortyResponse.getInfo().getNext().substring(rickAndMortyResponse.getInfo().getNext().indexOf("=")+1));
            Assert.assertSame(validPage == 0 ? validPage + 2 : validPage + 1, actualNextPage);

        } else if(rickAndMortyResponse.getInfo().getNext() == null) { //Última página
            //Comprobamos que la página anterior no sea nula
            Assert.assertNotNull(rickAndMortyResponse.getInfo().getPrev());

            actualPrevPage = Integer.parseInt(rickAndMortyResponse.getInfo().getPrev().substring(rickAndMortyResponse.getInfo().getPrev().indexOf("=")+1));
            Assert.assertSame(validPage - 1, actualPrevPage);
        } else { //Página intermedia
            //Obtenemos los valores devueltos en formato numérico
            actualPrevPage = Integer.parseInt(rickAndMortyResponse.getInfo().getPrev().substring(rickAndMortyResponse.getInfo().getPrev().indexOf("=")+1));
            actualNextPage = Integer.parseInt(rickAndMortyResponse.getInfo().getNext().substring(rickAndMortyResponse.getInfo().getNext().indexOf("=")+1));

            //Comprobamos los valores devueltos
            Assert.assertSame(validPage - 1, actualPrevPage);
            Assert.assertSame(validPage + 1, actualNextPage);
        }
    }


    @When("we get an invalid page an error is returned")
    public void weGetAnInvalidPageAnErrorIsReturned() {
        //Obtenemos todos los personajes de una página inexistente
        Call<RickAndMortyResponse> call = rickAndMortyService.getCharactersByPage(invalidPage);

        try {
            Response<RickAndMortyResponse> response = call.execute();
            if(response.isSuccessful()) { // 200: OK
                rickAndMortyResponse = response.body();
            } else { // 404: Not found
                //Devolvemos true al ser una página inválida
                //TODO: Podría ser mejor comprobar el mensaje, pero aprovecho el código de error de la respuesta
                Assert.assertEquals(404, response.code());
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

package com.actividad.booksPayments.Client;

import com.actividad.booksPayments.controller.model.CreatePurchaseRequest;
import com.actividad.booksPayments.data.model.PurchaseDetails;
import com.actividad.booksPayments.data.utils.BookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookCatalogueClient {

    private static final String MSJ_STOCK_UPDATE = "Stock actualizado correctamente";
    private static final String MSJ_ERROR_BOOK_NOT_EXIST_OR_NOT_STOCK = "Error libro no existe o no tiene stock:";

    private static final int STOCK_CERO = 0;
    private static final String MENSAJE_EL_LIBRO_NO_ES_VISIBLE_POR_LO_TANTO_NO_SE_PUEDE_COMPRAR = "El libro  %s no es visible, por lo tanto, no se puede comprar.";
    private static final String ERROR_GET_STOCK_CLIENT_ERROR_BOOK_WITH_ID = "Get stock - Client Error: %s, Book with ID %s";
    private static final String ERROR_GET_STOCK_SERVER_ERROR_BOOK_WITH_ID = "Get stock - Server Error: %s, Book with ID %s";
    private static final String ERROR_GET_STOCK_ERROR_BOOK_WITH_ID= "Get stock - Error: %s, Book with ID %s";
    private static final String ERROR_UPDATE_STOCK_CLIENT_ERROR_BOOK_WITH_ID = "Update stock - Client Error: %s, Book with ID %s";
    private static final String ERROR_UPDATE_STOCK_SERVER_ERROR_BOOK_WITH_ID = "Update stock - Server Error: %s, Book with ID %s";
    private static final String ERROR_UPDATE_STOCK_ERROR_BOOK_WITH_ID = "Update stock - Error: %s, Book with ID %s";

    @Value("${catalogueService.url}")
    private String urlBookService;

    private final WebClient.Builder webClient;

    public void validateStock(CreatePurchaseRequest request) {
        for (int i = 0; i < request.getPurchaseDetails().size(); i++) {
            PurchaseDetails details = request.getPurchaseDetails().get(i);
            if(details.getBookCount() > getBookStock(details.getBookId())){
                throw new RuntimeException(MSJ_ERROR_BOOK_NOT_EXIST_OR_NOT_STOCK + details.getBookName());
            }
        }
    }

    public void updateBooks(CreatePurchaseRequest request) {
        for (int i = 0; i < request.getPurchaseDetails().size(); i++) {
            PurchaseDetails details = request.getPurchaseDetails().get(i);
            updateBookStock(details.getBookId(),getBookStock(details.getBookId()) - details.getBookCount());
        }
    }

    public Integer getBookStock(String id) {
        try {
            String url = urlBookService + id;
            log.info("Consultando stock para el libro ID: {}", id);

            BookResponse response = webClient.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(BookResponse.class).block();

            if(!response.getVisible()){
                throw new RuntimeException(String.format(MENSAJE_EL_LIBRO_NO_ES_VISIBLE_POR_LO_TANTO_NO_SE_PUEDE_COMPRAR, response.getTitle()));
            }
            
            return (response != null) ? response.getStock() : STOCK_CERO;

        } catch (HttpClientErrorException e) {
            throw new RuntimeException(String.format(ERROR_GET_STOCK_CLIENT_ERROR_BOOK_WITH_ID, e.getStatusCode(), id));
        } catch (HttpServerErrorException e) {
            throw new RuntimeException(String.format(ERROR_GET_STOCK_SERVER_ERROR_BOOK_WITH_ID, e.getStatusCode(), id));
        } catch (Exception e) {
            throw new RuntimeException(String.format(ERROR_GET_STOCK_ERROR_BOOK_WITH_ID, e.getMessage(), id));
        }
    }

    public void updateBookStock(String id, Integer newStock) {
        try {
            String url =  urlBookService + id;
            log.info("Actualizando stock para el libro ID: {} a {}", id, newStock);

            BookResponse updateBody = new BookResponse();
            updateBody.setStock(newStock);

            webClient.build()
                    .patch()
                    .uri(url)
                    .bodyValue(updateBody)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info(MSJ_STOCK_UPDATE);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException(String.format(ERROR_UPDATE_STOCK_CLIENT_ERROR_BOOK_WITH_ID, e.getStatusCode(), id));
        } catch (HttpServerErrorException e) {
            throw new RuntimeException(String.format(ERROR_UPDATE_STOCK_SERVER_ERROR_BOOK_WITH_ID, e.getStatusCode(), id));
        } catch (Exception e) {
            throw new RuntimeException(String.format(ERROR_UPDATE_STOCK_ERROR_BOOK_WITH_ID, e.getMessage(), id));
        }
    }

}

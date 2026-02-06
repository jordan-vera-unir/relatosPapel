package com.actividad.booksPayments.Client;

import com.actividad.booksPayments.controller.model.CreatePurchaseRequest;
import com.actividad.booksPayments.data.model.PurchaseDetails;
import com.actividad.booksPayments.data.utils.BookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookCatalogueClient {

    @Value("${catalogueService.url}")
    private String urlBookService;

    private final WebClient.Builder webClient;

    public Integer getBookStock(String id) {
        try {
            String url = urlBookService + id;
            System.out.println("url:"+url);
            log.info("Consultando stock para el libro ID: {}", id);
            BookResponse response = webClient.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(BookResponse.class).block();

            return (response != null) ? response.getStock() : 0;

        } catch (Exception e) {
            log.error("Error al obtener stock del libro {}: {}", id, e.getMessage());
            return null;
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

            log.info("Stock actualizado correctamente");

        } catch (Exception e) {
            log.error("Error al actualizar stock del libro {}: {}", id, e.getMessage());
        }
    }


    public void validateStock(CreatePurchaseRequest request) {
        for (int i = 0; i < request.getPurchaseDetails().size(); i++) {
            PurchaseDetails details = request.getPurchaseDetails().get(i);
            if(details.getBookCount() > getBookStock(details.getBookId())){
                throw new RuntimeException("Error libro no existe o no tiene stock:"+details.getBookName());
            }
        }
    }

    public void updateBooks(CreatePurchaseRequest request) {
        for (int i = 0; i < request.getPurchaseDetails().size(); i++) {
            PurchaseDetails details = request.getPurchaseDetails().get(i);
           updateBookStock(details.getBookId(),getBookStock(details.getBookId()) - details.getBookCount());
        }
    }

}

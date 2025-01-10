package org.kunekune.PiglioTech.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.model.google.GoogleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.Optional;

@WebFluxTest(GoogleBooksDAO.class)
public class GoogleBooksDaoTest {

    private GoogleBooksDAO googleBooksDAO;
    private MockWebServer mockWebServer;
    private ObjectMapper mapper = new ObjectMapper();

    private static String singleBookJsonResponse = """
            {
              "kind": "books#volumes",
              "totalItems": 1,
              "items": [
                {
                  "kind": "books#volume",
                  "id": "IrB8iS6tKvAC",
                  "etag": "MnB5wJcb8cI",
                  "selfLink": "https://www.googleapis.com/books/v1/volumes/IrB8iS6tKvAC",
                  "volumeInfo": {
                    "title": "Jane Eyre",
                    "authors": [
                      "Charlotte Brontë"
                    ],
                    "publisher": "Random House",
                    "publishedDate": "2007",
                    "description": "As an orphan, Jane's childhood is not an easy one but her independence and strength of character keep her going through the miseries inflicted by cruel relatives and a brutal school. However, her biggest challenge is yet to come. Taking a job as a governess in a house full of secrets, for a passionate man she grows more and more attracted to, ultimately forces Jane to call on all her resources in order to hold on to her beliefs.",
                    "industryIdentifiers": [
                      {
                        "type": "ISBN_13",
                        "identifier": "9780099511120"
                      },
                      {
                        "type": "ISBN_10",
                        "identifier": "0099511126"
                      }
                    ],
                    "readingModes": {
                      "text": false,
                      "image": false
                    },
                    "pageCount": 562,
                    "printType": "BOOK",
                    "categories": [
                      "Classical fiction"
                    ],
                    "maturityRating": "NOT_MATURE",
                    "allowAnonLogging": false,
                    "contentVersion": "0.14.7.0.preview.0",
                    "panelizationSummary": {
                      "containsEpubBubbles": false,
                      "containsImageBubbles": false
                    },
                    "imageLinks": {
                      "smallThumbnail": "http://books.google.com/books/content?id=IrB8iS6tKvAC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                      "thumbnail": "http://books.google.com/books/content?id=IrB8iS6tKvAC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                    },
                    "language": "en",
                    "previewLink": "http://books.google.co.uk/books?id=IrB8iS6tKvAC&printsec=frontcover&dq=isbn:9780099511120&hl=&cd=1&source=gbs_api",
                    "infoLink": "http://books.google.co.uk/books?id=IrB8iS6tKvAC&dq=isbn:9780099511120&hl=&source=gbs_api",
                    "canonicalVolumeLink": "https://books.google.com/books/about/Jane_Eyre.html?hl=&id=IrB8iS6tKvAC"
                  },
                  "saleInfo": {
                    "country": "GB",
                    "saleability": "NOT_FOR_SALE",
                    "isEbook": false
                  },
                  "accessInfo": {
                    "country": "GB",
                    "viewability": "PARTIAL",
                    "embeddable": true,
                    "publicDomain": false,
                    "textToSpeechPermission": "ALLOWED",
                    "epub": {
                      "isAvailable": false
                    },
                    "pdf": {
                      "isAvailable": false
                    },
                    "webReaderLink": "http://play.google.com/books/reader?id=IrB8iS6tKvAC&hl=&source=gbs_api",
                    "accessViewStatus": "SAMPLE",
                    "quoteSharingAllowed": false
                  },
                  "searchInfo": {
                    "textSnippet": "A troubled childhood strengthens Jane Eyre&#39;s natural independence and spirit - which prove necessary when she becomes governess at Thornfield Hall."
                  }
                }
              ]
            }
            
            """;

    @BeforeEach
    void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        googleBooksDAO = new GoogleBooksDAO(mockWebServer.url("/").toString());
    }

    @AfterEach
    void teardown() throws Exception {
        mockWebServer.shutdown();
    }

    // Basic test with fake data (does not resemble Google data) to get used to WebFlux testing
    @Test
    public void initialTest() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setBody(singleBookJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<GoogleResult> bookMono = googleBooksDAO.fetchBookByIsbn("9780099511120");


        StepVerifier.create(bookMono)
                .expectNextMatches(b -> b.items()[0].volumeInfo().industryIdentifiers()[0].identifier().equals("9780099511120")
                && b.items()[0].volumeInfo().title().equals("Jane Eyre")
                && b.items()[0].volumeInfo().authors()[0].equals("Charlotte Brontë")
                && b.items()[0].volumeInfo().publishedDate().equals("2007")
                && ! b.items()[0].volumeInfo().description().isEmpty()
                && ! b.items()[0].volumeInfo().imageLinks().thumbnail().isEmpty())
                .verifyComplete();

    }

}

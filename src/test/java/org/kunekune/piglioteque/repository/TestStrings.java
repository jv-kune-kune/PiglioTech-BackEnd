package org.kunekune.piglioteque.repository;

public class TestStrings {

    public static final String NoTitleResponse = """
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

    public static final String NoAuthorsResponse = """
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
                    "authors": [],
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

    public static final String EmptyResponse = "{}";

    public static final String NoBooksJsonResponse = """
            {
              "kind": "books#volumes",
              "totalItems": 0,
              "items": []
              }
            """;

    public static final String SingleBookJsonResponse = """
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
}

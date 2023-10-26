package com.crawlling.backend;

import com.crawlling.backend.domain.interfaces.services.SearchManagerService;
import com.crawlling.backend.domain.interfaces.services.SearchService;
import com.crawlling.backend.domain.models.Search;
import com.crawlling.backend.presentation.DTO.GetSearchResponseDTO;
import com.crawlling.backend.presentation.DTO.PostSearchResponseDTO;
import com.crawlling.backend.presentation.DTO.SearchRequestDTO;
import com.crawlling.backend.services.SearchManagerImpl;
import com.crawlling.backend.services.SearchServiceImpl;
import com.crawlling.backend.services.WebCrawlerServiceImpl;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        WebCrawlerServiceImpl crawlerService = new WebCrawlerServiceImpl();
        SearchService searchService = new SearchServiceImpl(crawlerService);
        SearchManagerImpl searchManager = new SearchManagerImpl(searchService);

        port(4567);
        get("/crawl/:id", getSearchResults(searchManager));
        post("/crawl", postNewSearch(searchManager));
        init();
    }

    private static Route getSearchResults(SearchManagerService searchManager) {
        return (Request req, Response res) -> {
            String id = req.params("id");
            Search results = searchManager.getSearchById(id);
            if(results!= null) {
                GetSearchResponseDTO responseDTO = new GetSearchResponseDTO(
                        results.getId(),
                        results.getStatus(),
                        results.getFoundUrls()
                );

                Gson gson = new Gson();
                String jsonResults = gson.toJson(responseDTO);
                res.type("application/json");
                return jsonResults;
            } else {
                res.status(404);
                return "Pesquisa não encontrada";
            }
        };
    }

    private static Route postNewSearch(SearchManagerService searchManager) {
        return (Request req, Response res) -> {
            String body = req.body();
            Gson gson = new Gson();
            SearchRequestDTO requestDTO = gson.fromJson(body, SearchRequestDTO.class);

            if (requestDTO != null) {
                String keyword = requestDTO.getKeyword();

                if (keyword != null) {
                    Search search = searchManager.startNewSearch(keyword);
                    res.status(201);
                    res.type("application/json");
                    PostSearchResponseDTO postDto = new PostSearchResponseDTO(search.getId());
                    return gson.toJson(postDto);
                } else {
                    res.status(400);
                    return "A propriedade 'keyword' deve estar presente no corpo da solicitação.";
                }
            } else {
                res.status(400); // Código 400 para "Bad Request"
                return "O corpo da solicitação não está no formato JSON válido.";
            }
        };
    }

}

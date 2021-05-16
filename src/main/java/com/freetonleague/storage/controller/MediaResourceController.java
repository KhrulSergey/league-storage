package com.freetonleague.storage.controller;

import com.freetonleague.storage.domain.dto.MediaResourceDto;
import com.freetonleague.storage.service.RestMediaResourceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = MediaResourceController.BASE_PATH)
@RequiredArgsConstructor
@Api(value = "User Management Controller")
public class MediaResourceController {

    public static final String BASE_PATH = "/api/resource";
    public static final String PATH_GET_BY_HASH_AND_OWNER = "/by-hash-for-owner";
    public static final String PATH_GET_BY_HASH = "/by-hash";
    public static final String PATH_GET_BY_LEAGUE_ID = "/get-by-leagueId";
    public static final String PATH_CREATE = "/";

    /**
     * Parameter name for microservice data exchange
     */
    private static final String staticServiceTokenName = "service_token";

    private final RestMediaResourceFacade restMediaResourceFacade;

    @ApiOperation("Getting media resource by hash and ownerGUID")
    @GetMapping(path = PATH_GET_BY_HASH_AND_OWNER)
    public ResponseEntity<MediaResourceDto> getMediaResourceByHashAndGUID(@RequestParam("hash") String hash,
                                                                          @RequestParam("ownerGUID") String ownerGUID) {
        return new ResponseEntity<>(restMediaResourceFacade.getMediaResourceByHashAndOwnerGUID(hash, ownerGUID), HttpStatus.OK);
    }

    @ApiOperation("Getting media resource by hash (for micro service)")
    @GetMapping(path = PATH_GET_BY_HASH)
    public ResponseEntity<MediaResourceDto> getCurrent(@RequestParam(value = staticServiceTokenName, required = false) String serviceToken,
                                                       @RequestParam("hash") String hash) {
        return new ResponseEntity<>(restMediaResourceFacade.getMediaResourceByHash(hash), HttpStatus.OK);
    }


    @ApiOperation("Create and save new media resource on platform")
    @PostMapping(path = PATH_CREATE)
    public ResponseEntity<MediaResourceDto> createDocket(@RequestParam(value = staticServiceTokenName, required = false) String accessToken,
                                                         @RequestBody MediaResourceDto mediaResourceDto) {
        return new ResponseEntity<>(restMediaResourceFacade.addResource(mediaResourceDto), HttpStatus.CREATED);
    }
}

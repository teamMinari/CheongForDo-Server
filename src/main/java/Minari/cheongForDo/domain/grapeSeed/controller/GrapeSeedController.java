package Minari.cheongForDo.domain.grapeSeed.controller;

import Minari.cheongForDo.domain.grapeSeed.dto.GrapeSeedCommandReq;
import Minari.cheongForDo.domain.grapeSeed.dto.GrapeSeedLoadRes;
import Minari.cheongForDo.domain.grapeSeed.dto.GrapeSeedUpdateReq;
import Minari.cheongForDo.domain.grapeSeed.service.GrapeSeedService;
import Minari.cheongForDo.global.response.Response;
import Minari.cheongForDo.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gpse")
@RequiredArgsConstructor
@Tag(name = "GRAPESEED", description = "grape seed API")
public class GrapeSeedController {

    private final GrapeSeedService grapeSeedService;

    @GetMapping("{gpseId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "포도씨 일일 조회")
    public ResponseData<GrapeSeedLoadRes> findGrapeSeed(@PathVariable Long gpseId) {
        return grapeSeedService.findGrapeSeed(gpseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "포도씨 등록")
    public Response createGrapeSeed(@RequestBody GrapeSeedCommandReq commandReq) {
        return grapeSeedService.createGrapeSeed(commandReq);
    }

    @PatchMapping("{gpseId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "포도씨 업데이트")
    public Response updateGrapeSeed(@PathVariable Long gpseId, @RequestBody GrapeSeedUpdateReq updateReq) {
        return grapeSeedService.updateGrapeSeed(gpseId, updateReq);
    }

    @PatchMapping("/ver/{gpseId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "포도씨 검증 업데이트")
    public Response updateGrapeSeedVerification(@PathVariable Long gpseId, @RequestParam String verification) {
        return grapeSeedService.updateGrapeSeedVerification(gpseId, verification);
    }

    @DeleteMapping("{gpseId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "포도씨 삭제")
    public Response deleteGrapeSeed(@PathVariable Long gpseId) {
        return grapeSeedService.deleteGrapeSeed(gpseId);
    }

}

package Minari.cheongForDo.domain.grape.controller;

import Minari.cheongForDo.domain.grape.dto.GrapeCommandReq;
import Minari.cheongForDo.domain.grape.dto.GrapeLoadRes;
import Minari.cheongForDo.domain.grape.dto.GrapeUpdateReq;
import Minari.cheongForDo.domain.grape.service.GrapeService;
import Minari.cheongForDo.global.response.Response;
import Minari.cheongForDo.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gp")
@RequiredArgsConstructor
public class GrapeController {

    private final GrapeService grapeService;

    @GetMapping("{gpId}")
    public ResponseData<List<GrapeLoadRes>> findGrape(@PathVariable Long gpId) {
        return grapeService.findGrape(gpId);
    }

    @PostMapping
    public Response createGrape(@RequestBody GrapeCommandReq commandReq) {
        return grapeService.createGrape(commandReq);
    }

    @PatchMapping("{gpId}")
    public Response updateGrape(@PathVariable Long gpId, @RequestBody GrapeUpdateReq updateReq) {
        return grapeService.updateGrape(gpId, updateReq);
    }

    @DeleteMapping("{gpId}")
    public Response deleteGrape(@PathVariable Long gpId) {
        return grapeService.deleteGrape(gpId);
    }
}
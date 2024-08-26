package Minari.cheongForDo.domain.grapeSeed.service;

import Minari.cheongForDo.domain.grapeSeed.dto.GrapeSeedCommandReq;
import Minari.cheongForDo.domain.grapeSeed.dto.GrapeSeedUpdateReq;
import Minari.cheongForDo.domain.grapeSeed.entity.GrapeSeed;
import Minari.cheongForDo.domain.grapeSeed.repository.GrapeSeedRepository;
import Minari.cheongForDo.domain.member.authority.MemberAccountType;
import Minari.cheongForDo.domain.member.entity.MemberEntity;
import Minari.cheongForDo.domain.term.entity.Term;
import Minari.cheongForDo.global.auth.UserSessionHolder;
import Minari.cheongForDo.global.exception.CustomException;
import Minari.cheongForDo.global.response.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import static Minari.cheongForDo.global.exception.CustomErrorCode.GRAPESEED_NOT_EXIST;
import static Minari.cheongForDo.global.exception.CustomErrorCode.MEMBER_NOT_AUTHORITY;

@Service
@Transactional
@RequiredArgsConstructor
public class GrapeSeedService {

    private final GrapeSeedRepository grapeSeedRepository;
    private final UserSessionHolder userSessionHolder;

    public Response createGrapeSeed(GrapeSeedCommandReq commandReq) {

        MemberEntity writer = userSessionHolder.current();
        checkMemberAuthority(writer);

        grapeSeedRepository.save(commandReq.toEntity());

        return Response.of(HttpStatus.OK, "포도씨 생성 완료!");
    }

    public Response updateGrapeSeed(Long gpseId, GrapeSeedUpdateReq updateReq) {

        MemberEntity writer = userSessionHolder.current();
        checkMemberAuthority(writer);

        GrapeSeed getGrapeSeed = getGrapeSeed(gpseId);
        getGrapeSeed.update(updateReq);

        return Response.of(HttpStatus.OK, "포도씨 업데이트 완료!");
    }

    public Response deleteGrapeSeed(Long gpseId) { // like, learn 함께 삭제하는 로직 필요

        MemberEntity writer = userSessionHolder.current();
        checkMemberAuthority(writer);

        GrapeSeed getGrapeSeed = getGrapeSeed(gpseId);

        grapeSeedRepository.delete(getGrapeSeed);

        return Response.of(HttpStatus.OK, "포도씨 삭제 완료!");
    }

    private void checkMemberAuthority(MemberEntity writer) {
        if (writer.getAuthority() != MemberAccountType.ROLE_ADMIN) {
            throw new CustomException(MEMBER_NOT_AUTHORITY);
        }
    }

    private GrapeSeed getGrapeSeed(Long gpseId) {
        return grapeSeedRepository.findById(gpseId).orElseThrow(
                () -> new CustomException(GRAPESEED_NOT_EXIST));
    }

}
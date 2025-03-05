package Minari.cheongForDo.domain.term.entity;

import Minari.cheongForDo.domain.like.entity.Like;
import Minari.cheongForDo.domain.term.dto.TermCommandReq;
import Minari.cheongForDo.domain.term.model.enums.TermDifficulty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Term {

    // 용어 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termId;

    // 용어 이름
    @Column(nullable = false, unique = true)
    private String termNm;

    // 용어 내용
    @Column(nullable = false, length = 10000)
    private String termExplain;

    // 용어 난이도
    @Column
    @Enumerated(EnumType.STRING)
    private TermDifficulty termDifficulty;

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY)
    private List<Like> likes;

    public void update(TermCommandReq termRequestDTO) {
        this.termNm = termRequestDTO.getTermNm();
        this.termExplain = termRequestDTO.getTermExplain();
        this.termDifficulty = termRequestDTO.getTermDifficulty();
    }

    // 쉬운용어풀이 저장
    @Column(columnDefinition = "TEXT")  // 긴 문자열 저장 가능
    private String summary;

//    // 쉬운용어풀이 업데이트
//    public void updateSummary(String summary) {
//        this.summary = summary;
//    }
}

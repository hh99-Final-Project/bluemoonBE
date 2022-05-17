package com.sparta.bluemoon.temporary;

import com.sparta.bluemoon.post.PostService;
import com.sparta.bluemoon.post.requestDto.PostCreateRequestDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TemporaryController {
    private final TemporaryService temporaryService;
    private final TemporaryRepository temporaryRepository;
    private final PostService postService;

    //처음 게시글 작성시 임시저장
    @PostMapping("/api/temporary")
    public void saveTemporary(@RequestBody TemporaryRequestDto requestDto,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        temporaryService.saveTemporary(requestDto, userDetails.getUser());
    }

    //나의 임시저장 조회 list//페이징 처리 필요
    @GetMapping("/api/mytemporarys/{page}")
    public List<TemporaryResponseDto> getTemporarys(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable int page){
        return temporaryService.getTemporarys(userDetails, page);
    }

    //임시저장 상세조회
    @GetMapping("/api/mytemporary/{tempId}")
    public TemporaryResponseDto getTemporary(@PathVariable Long tempId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        Temporary temporary = temporaryRepository.findById(tempId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 글입니다.")
        );

        //글 쓴사람 체크
        temporaryService.checkWriter(temporary, userDetails);

        return new TemporaryResponseDto(temporary);

    }


    //임시저장 삭제
    @DeleteMapping("/api/mytemporary/{tempId}")
    public void deleteTemporary(@PathVariable Long tempId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        Temporary temporary = temporaryRepository.findById(tempId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 글입니다.")
        );

        //글 쓴사람 체크
        temporaryService.checkWriter(temporary, userDetails);

        temporaryRepository.deleteById(tempId);
    }


    //임시글 수정 후 임시저장
    @PutMapping("/api/mytemporary/{tempId}")
    public void updateTemporary(@PathVariable Long tempId,
                                @RequestBody TemporaryRequestDto requestDto,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        temporaryService.updateTemporary(tempId, requestDto, userDetails);
    }


    //임시글 수정 후 게시글에 저장 후 삭제
    @PostMapping("/api/mytemporary/{tempId}")
    public void uploadPost(@PathVariable Long tempId,
                           @RequestBody PostCreateRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){

        Temporary temporary = temporaryRepository.findById(tempId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 글입니다.")
        );

        //글 쓴사람 체크
        temporaryService.checkWriter(temporary, userDetails);

        postService.createWithoutVoice(requestDto, userDetails.getUser());
        temporaryRepository.deleteById(tempId);

    }

}


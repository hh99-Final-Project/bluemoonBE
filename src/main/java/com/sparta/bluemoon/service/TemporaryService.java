package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Temporary;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.TemporaryRequestDto;
import com.sparta.bluemoon.dto.response.TemporaryResponseDto;
import com.sparta.bluemoon.repository.TemporaryRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemporaryService {
    private final TemporaryRepository temporaryRepository;

    //임시 글 저장
    public void saveTemporary(TemporaryRequestDto requestDto, User user){
        Temporary temporary = new Temporary(requestDto, user);
        temporaryRepository.save(temporary);
    }

    //나의 임시글 조회
    public List<TemporaryResponseDto> getTemporarys(UserDetailsImpl userDetails, int page) {
        //service로 옮기기 리팩터링+페이징 처리
        int display = 5;
        Pageable pageable = PageRequest.of(page,display);
        Page<Temporary> temporaries = temporaryRepository.findByUser(userDetails.getUser(),pageable);

        List<TemporaryResponseDto> responseDtos = new ArrayList<>();
        for(Temporary temp : temporaries){
            TemporaryResponseDto responseDto = new TemporaryResponseDto(temp);
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }


    //임시 글 수정 후 임시 저장
    @Transactional
    public void updateTemporary(Long tempId, TemporaryRequestDto requestDto, UserDetailsImpl userDetails) {

        Temporary temporary = temporaryRepository.findById(tempId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 글입니다.")
        );

        //글 쓴사람 체크
        checkWriter(temporary, userDetails);
        temporary.update(requestDto);
    }


    public void checkWriter (Temporary temporary,UserDetailsImpl userDetails){
        if(!temporary.getUser().equals(userDetails.getUser())){
            throw new IllegalArgumentException("글 작성자가 아닙니다.");
        }
    }

}

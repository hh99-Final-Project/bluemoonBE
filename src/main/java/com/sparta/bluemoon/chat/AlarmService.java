package com.sparta.bluemoon.chat;

import com.sparta.bluemoon.chat.Alarm;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.chat.responseDto.AlarmDto;
import com.sparta.bluemoon.chat.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    private static final String SORT_PROPERTIES = "id";

    private static final int ALARM_COUNT = 10;

    public List<AlarmDto> getAlarms(int pageId, User user) {
        //알람의 개수가 paging 처리해야하는 개수보다 적을 경우
        int alarmSize = Math.min(alarmRepository.findByUser(user).size(), ALARM_COUNT);
        try{
            Pageable pageable = PageRequest.of(pageId, alarmSize, Sort.by((Sort.Direction.DESC), SORT_PROPERTIES));
            //유저가 가진 알람 불러오기
            Page<Alarm> pagedAlarms = alarmRepository.findByUser(user, pageable);
            //Alarm 을 AlarmDto로 변환
            return converAlarmToAlarmDto(pagedAlarms);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
    //Alarm 을 AlarmDto로 변환하는 메소드
    private List<AlarmDto> converAlarmToAlarmDto(Page<Alarm> pagedAlarms) {
        List<AlarmDto> alarmDtos = new ArrayList<>();
        for(Alarm alarm : pagedAlarms){
            alarmDtos.add(new AlarmDto(alarm));
        }
        return alarmDtos;
    }
}

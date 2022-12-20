package com.triple.mileageservice.controller;

import com.triple.mileageservice.common.util.UUIDUtils;
import com.triple.mileageservice.domain.place.entity.Place;
import com.triple.mileageservice.domain.place.repository.PlaceRepository;
import com.triple.mileageservice.domain.user.entity.Users;
import com.triple.mileageservice.domain.user.repository.UsersRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UsersRepository usersRepository;
    private final PlaceRepository placeRepository;

    @PostMapping("/user")
    @Transactional
    public TestResponse addUser(@RequestBody String userId) {
        TestResponse testResponse = new TestResponse(userId);

        UUID userUUID = UUIDUtils.stringToUUID("body", userId);
        if (userUUID.toString().equals(userId)) {
            return saveUser(testResponse, userUUID);
        } else {
            return testResponse.response(TestResponseEnum.ILLEGAL_UUID);
        }
    }

    private TestResponse saveUser(TestResponse testResponse, UUID uuid) {
        try {
            usersRepository.save(new Users(uuid));
            return testResponse.response(TestResponseEnum.OK);
        } catch (Exception e) {
            return testResponse.response(TestResponseEnum.DUPLICATE_UUID);
        }
    }

    @PostMapping("/place")
    @Transactional
    public TestResponse addPlace(@RequestBody String placeId) {
        TestResponse testResponse = new TestResponse(placeId);

        UUID placeUUID = UUIDUtils.stringToUUID("body", placeId);
        if (placeUUID.toString().equals(placeId)) {
            return savePlace(testResponse, placeUUID);
        } else {
            return testResponse.response(TestResponseEnum.ILLEGAL_UUID);
        }
    }

    private TestResponse savePlace(TestResponse testResponse, UUID uuid) {
        try {
            placeRepository.save(new Place(uuid));
            return testResponse.response(TestResponseEnum.OK);
        } catch (Exception e) {
            return testResponse.response(TestResponseEnum.DUPLICATE_UUID);
        }
    }

    @Getter
    @Setter
    public static class TestResponse {

        private int rspCode;
        private String rspMessage;
        private Object result;

        public TestResponse(Object result) {
            this.result = result;
        }

        public TestResponse response(TestResponseEnum en) {
            this.rspCode = en.getRspCode();
            this.rspMessage = en.name();

            return this;
        }
    }

    @Getter
    public enum TestResponseEnum {
        OK(1),
        ILLEGAL_UUID(2),
        DUPLICATE_UUID(3);

        private final int rspCode;

        TestResponseEnum(int rspCode) {
            this.rspCode = rspCode;
        }
    }
}

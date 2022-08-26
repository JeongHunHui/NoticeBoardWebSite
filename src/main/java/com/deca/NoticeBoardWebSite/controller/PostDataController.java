package com.deca.NoticeBoardWebSite.controller;

import com.deca.NoticeBoardWebSite.domain.PostData;
import com.deca.NoticeBoardWebSite.service.PostDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController // 스프링 컨테이너에 controller 임을 알려주는 표시
                // + ResponseBody 기능
@RequestMapping(value = "/api/postData")
//
public class PostDataController {
    private final PostDataService postDataService;

    @Autowired
    // Autowired: 의존성 부여
    // 스프링 컨테이너에서 postDataService 가져옴
    // -> postDataService 도 컨테이너에 등록이 되어있어야함
    public PostDataController(PostDataService postDataService){
        this.postDataService = postDataService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getDataById")
    public PostData getPostDataById(@RequestParam(value="id", defaultValue="0") Long id){
        Optional<PostData> postData = postDataService.findById(id);
        if(postData.isPresent()) return postData.get();
        throw new IllegalStateException("잘못된 id 값입니다.");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getDataByType")
    public List<PostData> getPostDataByType(@RequestParam(value="type") String type){
        List<PostData> postDataList = type.equals("all") ? postDataService.findAll() : postDataService.findByType(type);
        return postDataList;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getCount")
    public Long getPostCount(){
        return postDataService.getPostCount();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getTypes")
    public List<String[]> getTypeKeyValues(){
        return postDataService.getTypeKeyValues();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/upload")
    public String savePostData(@RequestBody PostData postData){
        postDataService.uploadPost(postData);
        return "success";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/update/postLike")
    public ResponseEntity<String> updatePostLike(@RequestParam(value="id") Long id){
        return postDataService.updatePostLike(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/update/disLike")
    public ResponseEntity<String> updateDisLike(@RequestParam(value="id") Long id){
        return postDataService.updateDisLike(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/update/viewCount")
    public ResponseEntity<String> updateViewCount(@RequestParam(value="id") Long id){
        return postDataService.updateViewCount(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestBody MultipartFile files){
        // 시간과 original Filename 으로 매핑 시켜서 src 주소를 만들어 낸다.
        Date date = new Date();
        StringBuilder sb = new StringBuilder();

        // file image 가 없을 경우
        if (files.isEmpty()) {
            sb.append("none");
        } else {
            sb.append(date.getTime());
            sb.append(files.getOriginalFilename());
        }
        String path = "/Users/jeonghunhui/Documents/MyProjects/WebProject/NoticeBoardWebSite/src/main/frontend/public/testImages/" + sb.toString();
        if (!files.isEmpty()) {
            File dest = new File(path);
            try {
                files.transferTo(dest);
                return "/testImages/" + sb.toString();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // db에 파일 위치랑 번호 등록
        }
        return null;
    }
}

package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.TodoRecommendationDto;
import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {
    private final TodoRepository todoRepository;
    private final  RestTemplate restTemplate = new RestTemplate();
    private String apiKey = "AIzaSyDSJL3f-34LqvPoaFvwObykImgSTYaafPw";
    public List<String> getTopTodoPatterns(String userId){
        List<Object[]> result = todoRepository.findTopFrequentContentsByUser(userId);
        return result.stream()
                .map(obj -> (String)obj[0])
                .limit(5)
                .collect(Collectors.toList());
    }
    public String getTodoRecommendation(String prompt){
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text",prompt)
                                ))
                )
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        // 응답에서 텍스트 추출
        Map result = (Map)((List) response.getBody().get("candidates")).get(0);
        Map content = (Map) result.get("content");
        List<Map> parts =(List<Map>) content.get("parts");
        return (String) parts.get(0).get("text");
    }
    public String makePrompt(String userId){
        List<String> frequentTodos = getTopTodoPatterns(userId);
        StringBuilder prompt = new StringBuilder();
        for(String task : frequentTodos){
            prompt.append("- ").append(task).append("\n");
        }
        prompt.append("\n위의 할 일은 자주한 할 일이고 이 것과 오늘의 날씨, 지금 시간대, 날짜를 참고해서 할 일을 3개 제안해줘");
        return prompt.toString();
    }
}

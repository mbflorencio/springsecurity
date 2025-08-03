package br.mf.demospringsecurity.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.mf.demospringsecurity.dto.RequestItemDTO;

public class RequestDTO {
    private Long id;
    private String requestText;
    private LocalDateTime requestDate;
    private String responseText;
    private Long userId;
    private List<RequestItemDTO> items;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<RequestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RequestItemDTO> items) {
        this.items = items;
    }
}

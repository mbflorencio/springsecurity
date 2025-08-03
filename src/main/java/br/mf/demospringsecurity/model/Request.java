package br.mf.demospringsecurity.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import br.mf.demospringsecurity.model.RequestProduct;


/**
 * Request entity mapped to requests table.
 */

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requesttext", columnDefinition = "TEXT", nullable = false)
    private String requestText;

    @Column(name = "requestdate", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "responsetext")
    private String responseText;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestProduct> items = new ArrayList<>();

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<RequestProduct> getItems() {
        return items;
    }

    public void setItems(List<RequestProduct> items) {
        this.items = items;
    }
}

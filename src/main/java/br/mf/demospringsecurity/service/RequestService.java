package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.Request;
import br.mf.demospringsecurity.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository repository;

    @Autowired
    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    public Request save(Request request) {
        return repository.save(request);
    }

    public List<Request> findAll() {
        return repository.findAll();
    }

    public List<Request> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Optional<Request> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

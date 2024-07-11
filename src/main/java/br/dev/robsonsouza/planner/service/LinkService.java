package br.dev.robsonsouza.planner.service;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.LinkCreateResponse;
import br.dev.robsonsouza.planner.dto.LinkData;
import br.dev.robsonsouza.planner.dto.LinkRequestPayload;
import br.dev.robsonsouza.planner.model.Link;
import br.dev.robsonsouza.planner.model.Trip;
import br.dev.robsonsouza.planner.repository.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkService {
    
    private LinkRepository repository;
    
    public LinkCreateResponse registerLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload, trip);
        repository.saveAndFlush(newLink);
        
        return new LinkCreateResponse(newLink.getId());
    }
    
    public List<LinkData> getAllLinksFromTrip(UUID id) {
        return repository.findLinksByTripId(id)
                .stream()
                .map(activity -> new LinkData(activity.getId(), activity.getTitle(),
                                              activity.getUrl()))
                .toList();
    }
}

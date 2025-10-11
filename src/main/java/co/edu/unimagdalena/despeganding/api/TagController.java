package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController @RequiredArgsConstructor @Validated
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagCreateRequest request, UriComponentsBuilder uriBuilder) {
        var body = tagService.createTag(request);
        var location = uriBuilder.path("/api/v1/tags/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTag(id));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> list() {
        return ResponseEntity.ok(tagService.listAllTags());
    }

    @GetMapping(params = "names") //RequestParam is made mostly for filtering info
    public ResponseEntity<List<TagResponse>> listByNames(@RequestParam List<String> names) {
        return ResponseEntity.ok(tagService.listTagsByNameIn(names));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}

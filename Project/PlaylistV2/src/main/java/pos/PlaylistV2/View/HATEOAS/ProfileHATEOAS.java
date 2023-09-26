package pos.PlaylistV2.View.HATEOAS;

import org.springframework.hateoas.Link;
import pos.PlaylistV2.Controller.PlaylistController;
import pos.PlaylistV2.Controller.ProfileController;
import pos.PlaylistV2.View.DTOs.PlaylistDto;
import pos.PlaylistV2.View.DTOs.ProfileDto;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ProfileHATEOAS {
    public static void createHATEOAS(ProfileDto profile){
        profile.add(linkTo(methodOn(ProfileController.class).get(profile.getId())).withSelfRel());
        profile.add(linkTo(methodOn(ProfileController.class).listProfiles()).withRel("parent"));
    }
    public static Link createHATEOAS(List<ProfileDto> profiles){
        profiles.forEach(ProfileHATEOAS::createHATEOAS);
        return linkTo(methodOn(ProfileController.class).listProfiles()).withSelfRel();
    }
}

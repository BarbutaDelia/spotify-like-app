package pos.PlaylistV2.Model.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pos.PlaylistV2.Model.Entities.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String> {
}

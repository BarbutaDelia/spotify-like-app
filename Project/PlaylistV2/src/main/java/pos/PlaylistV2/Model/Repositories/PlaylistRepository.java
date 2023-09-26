package pos.PlaylistV2.Model.Repositories;

import pos.PlaylistV2.Model.Entities.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
}

package pl.joboffers.domain.loginandregister;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Document("users")
record User(
        @Id
        String userId,
        @Field("username")
        @Indexed(unique = true)
        String username,
        @Field("password")
        String password
) {
}

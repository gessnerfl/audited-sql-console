package de.gessnerfl.auditedsqlconsole.domain.database.connection.repository;

import de.gessnerfl.auditedsqlconsole.domain.database.connection.model.DatabaseConnection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DatabaseServerRepository extends MongoRepository<DatabaseConnection,String>{
}

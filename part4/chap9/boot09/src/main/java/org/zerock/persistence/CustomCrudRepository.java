package org.zerock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.WebBoard;

public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard{

}

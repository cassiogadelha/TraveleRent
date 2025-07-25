package verso.caixa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import verso.caixa.dto.BookingRequestBody;
import verso.caixa.model.BookingModel;

@Mapper(componentModel = "cdi")
public interface BookingMapper {
    @Mapping(target = "status", constant = "CREATED")
    BookingModel toEntity(BookingRequestBody dto);
}

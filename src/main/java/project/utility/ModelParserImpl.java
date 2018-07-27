package project.utility;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class ModelParserImpl implements ModelParser {

    private ModelMapper modelMapper;

    public ModelParserImpl() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public <S, D> D convert(S source, Class<D> destinationClass) {
        D convertedObject = null;
        convertedObject = modelMapper.map(source, destinationClass);
        return convertedObject;
    }
}

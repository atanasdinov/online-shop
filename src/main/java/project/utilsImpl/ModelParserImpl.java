package project.utilsImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import project.utils.ModelParser;

@Component
public class ModelParserImpl implements ModelParser {

    private ModelMapper modelMapper;

    public ModelParserImpl() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public <S, D> D convert(S source, Class<D> destinationClass) {
        return this.modelMapper.map(source, destinationClass);
    }

}
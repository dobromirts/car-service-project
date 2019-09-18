package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.entities.Part;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.repository.PartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;

    public PartServiceImpl(PartRepository partRepository, ModelMapper modelMapper) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PartServiceModel addPart(PartServiceModel partServiceModel) {
        Part part=this.modelMapper.map(partServiceModel,Part.class);
        return this.modelMapper.map(this.partRepository.saveAndFlush(part),PartServiceModel.class);
    }

    @Override
    public List<PartServiceModel> findAllParts() {
        return this.partRepository.findAll().stream().map(p->this.modelMapper.map(p,PartServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public PartServiceModel findPartById(String id) {
        Part part=this.partRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid part"));
        return this.modelMapper.map(part,PartServiceModel.class);
    }

    @Override
    public PartServiceModel editPart(PartServiceModel partServiceModel) {
        Part part=this.partRepository.findById(partServiceModel.getId()).orElseThrow(()->new IllegalArgumentException("Incorrect Part!"));
        part.setName(partServiceModel.getName());
        part.setDescription(partServiceModel.getDescription());
        part.setPrice(partServiceModel.getPrice() );
        part.setCategories(partServiceModel.getCategories().stream().map(c->this.modelMapper.map(c, Category.class)).collect(Collectors.toList()));

        return this.modelMapper.map(this.partRepository.saveAndFlush(part),PartServiceModel.class);
    }

    @Override
    public void deletePart(String id) {
        this.partRepository.deleteById(id);
    }
}

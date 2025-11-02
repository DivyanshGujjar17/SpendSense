package com.MoneyManager.SpendSense.Service;

import com.MoneyManager.SpendSense.Dto.CategoryDTO;
import com.MoneyManager.SpendSense.Entity.CategoryEntity;
import com.MoneyManager.SpendSense.Entity.ProfileEntity;
import com.MoneyManager.SpendSense.Repositiory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    public CategoryDTO savecategory(CategoryDTO categoryDTO){
        ProfileEntity profileEntity=profileService.getcurrentprofile();
if(categoryRepository.existsByNameAndProfile_Id(categoryDTO.getName(), profileEntity.getId())){
    throw  new RuntimeException("Category with this Name is already exist");
}
CategoryEntity newcategory=toEntity(categoryDTO,profileEntity);
newcategory=categoryRepository.save(newcategory);;
return toDTO(newcategory);

    }
    public List<CategoryDTO> getcategoriesbyTypeforCurrentuser(String Type){
        ProfileEntity profileEntity=profileService.getcurrentprofile();
    List<CategoryEntity> categoriesbytype= categoryRepository.findByTypeAndProfile_Id(Type,profileEntity.getId());
    return categoriesbytype.stream().map(this::toDTO).toList();
    }
    public List<CategoryDTO> getallcategoriesforCurrentUser(){
        ProfileEntity profileEntity=profileService.getcurrentprofile();
        List<CategoryEntity> categories=categoryRepository.findByProfile_Id(profileEntity.getId());
        return categories.stream().map(this::toDTO).toList();
    }
    public CategoryDTO updateCategory(long CategoryId,CategoryDTO dto){
        ProfileEntity profileEntity=profileService.getcurrentprofile();
        CategoryEntity ExistingCategory=categoryRepository.findByIdAndProfile_Id(CategoryId,profileEntity.getId()).orElseThrow(()->new RuntimeException("Category Not Found Or Accessible"));
      ExistingCategory.setName(dto.getName());
      ExistingCategory.setIcon(dto.getIcon());
      ExistingCategory=categoryRepository.save(ExistingCategory);
      return toDTO(ExistingCategory);
    }
    private CategoryEntity toEntity(CategoryDTO categoryDTO , ProfileEntity profile){
return CategoryEntity.builder().
        name(categoryDTO.getName())
        .icon(categoryDTO.getIcon())
        .type(categoryDTO.getType())
        .profile(profile).build();
    }
private CategoryDTO toDTO(CategoryEntity categoryEntity){
        return CategoryDTO.builder().
                id(categoryEntity.getId())
                .profileid(String.valueOf(categoryEntity.getProfile()!=null ? categoryEntity.getProfile().getId():null)).
                createdAt(categoryEntity.getCreatedAt()).
    name(categoryEntity.getName()).
                icon(categoryEntity.getIcon()).
                updatedAt(categoryEntity.getUpdatedAt()).
                type(categoryEntity.getType())
                .build();
}

}

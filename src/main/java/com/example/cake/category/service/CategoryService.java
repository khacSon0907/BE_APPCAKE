package com.example.cake.category.service;

import com.example.cake.category.dto.CategoryCreateRequest;
import com.example.cake.category.dto.CategoryDelete;
import com.example.cake.category.dto.CategoryUpdate;
import com.example.cake.category.model.Categories;
import com.example.cake.category.repository.CategoryRepository;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public ResponseMessage<Categories> createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByCode(request.getCode())) {
            return new ResponseMessage<>(false, "Mã code của loại bánh đã tồn tại", null);
        }

        if (categoryRepository.existsByName(request.getName())) {
            return new ResponseMessage<>(false, "Tên loại bánh đã tồn tại", null);
        }

        Categories category = Categories.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryRepository.save(category);
        return new ResponseMessage<>(true, "Tạo loại bánh thành công", category);
    }

    public ResponseMessage<List<Categories>> getAllCategory(){
        List<Categories> categoriesList = categoryRepository.findAll();
        return new ResponseMessage<>(true,"Danh sách categories ", categoriesList);
    }

    public  ResponseMessage<Categories> updateCategory(CategoryUpdate request){
        Optional<Categories> categoriesOptional = categoryRepository.findByCode(request.getCode());
        if(categoriesOptional.isEmpty()){
            return  new ResponseMessage<>(false," Mã sản phẩm không tồn tại ", null);
        }
         Categories categories = categoriesOptional.get();
         categories.setName(request.getName());
         categories.setDescription(request.getDescription());
         categoryRepository.save(categories);
        return  new ResponseMessage<>(true," Update category thành công ",categories);
    }

    public ResponseMessage<String> deleteCategory(CategoryDelete categoryDelete){
        Optional<Categories> categories = categoryRepository.findByCode(categoryDelete.getCode());

        if(categories.isEmpty()){
            return  new ResponseMessage<>(false," Mã sản phẩm không tồn tại ", null);
        }

        categoryRepository.delete(categories.get());

        return  new ResponseMessage<>(true ,"Xoá thành công ", null);
    }
}

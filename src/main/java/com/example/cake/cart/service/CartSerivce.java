package com.example.cake.cart.service;


import com.example.cake.auth.model.User;
import com.example.cake.auth.repository.UserRepository;
import com.example.cake.cart.model.Cart;
import com.example.cake.cart.model.CartItem;
import com.example.cake.cart.repository.CartRepository;
import com.example.cake.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CartSerivce {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
//
//    public ResponseMessage<Cart> addToCart(Cart cart) {
//        Optional<User> optionalUser = userRepository.findById(cart.getUserId());
//
//       if(optionalUser.isEmpty()){
//           return  new ResponseMessage<>(false,"User không tồn tại",null);
//       }
//       else {
//           Cart cartNew  = new Cart()
//       }
//
//
//    }

}

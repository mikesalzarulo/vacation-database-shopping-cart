package com.example.demo.services;

import com.example.demo.dao.*;
import com.example.demo.entities.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;
    private final ExcursionRepository excursionRepository;
    private final VacationRepository vacationRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, ExcursionRepository excursionRepository, VacationRepository vacationRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.excursionRepository = excursionRepository;
        this.vacationRepository = vacationRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        Customer customer = purchase.getCustomer();
        Cart cart = purchase.getCart();

        String orderTrackingNumber = generatedOrderTrackingNumber();
        cart.setOrderTrackingNumber(orderTrackingNumber);

        Vacation vacation = purchase.getCartItems()
                .stream()
                .findFirst()
                .map(CartItem::getVacation)
                .orElseThrow(() -> new IllegalArgumentException("Vacation null."));
        vacationRepository.save(vacation);

        cartRepository.save(cart);

        Optional.ofNullable(vacation.getExcursions())
                .ifPresent(excursions -> excursions.forEach(excursion -> {
                    if (excursion.getVacation() == null){
                        excursion.setVacation(vacation);
                    }
                    excursionRepository.save(excursion);
                }));
        purchase.getCartItems().forEach(cartItem -> {
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        });
        purchase.getCartItems().forEach(cartItem -> {
            Set<Excursion> excursionCartItems = cartItem.getExcursions();
            if (excursionCartItems != null) {
                excursionCartItems.forEach(excursion -> {
                    Excursion pExcursion = excursionRepository.findById(excursion.getId()).orElse(null);
                    if (pExcursion != null) {
                        pExcursion.getCartItems().add(cartItem);
                        excursionRepository.save(pExcursion);
                    }
                });
            }
        });

        cart.setCustomer(customer);
        cart.setStatusType(Cart.StatusType.ordered);
        cartRepository.save(cart);

        return new PurchaseResponse(orderTrackingNumber);

    }

    private String generatedOrderTrackingNumber() {

        return UUID.randomUUID().toString();

    }

}

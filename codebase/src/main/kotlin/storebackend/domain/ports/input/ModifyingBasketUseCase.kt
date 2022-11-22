package storebackend.domain.ports.input

import storebackend.adapters.input.rest.dto.UpdateBasketDto
import storebackend.domain.models.Basket

interface ModifyingBasketUseCase {
    fun modifyBasket(updateBasketDto: UpdateBasketDto): Basket
}
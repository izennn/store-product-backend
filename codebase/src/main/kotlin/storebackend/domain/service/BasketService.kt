package storebackend.domain.service

import storebackend.adapters.input.rest.dto.UpdateBasketDto
import storebackend.adapters.output.repository.InMemoryBasketRepositoryImpl
import storebackend.domain.models.Basket
import storebackend.domain.ports.input.ModifyingBasketUseCase
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class BasketService @Inject constructor(
    val repository: InMemoryBasketRepositoryImpl
) : ModifyingBasketUseCase {
    override fun modifyBasket(updateBasketDto: UpdateBasketDto): Basket {
        val updateBody = updateBasketDto.toDomain()
        return repository.updateBasket(updateBody)
    }
}
import { configureStore } from '@reduxjs/toolkit'

import cartItemsReducer from './shopping-cart/cartItemsSlide'
import categoryReducer from './category'

export const store = configureStore({
    reducer: {
        cartItems: cartItemsReducer,
        category: categoryReducer
    },
})
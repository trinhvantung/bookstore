import { createSlice } from '@reduxjs/toolkit'


const initialState = {
    show: false
}

export const categorySlice = createSlice({
    name: 'category',
    initialState,
    reducers: {
        change: (state, action) => {
            const current = state.show
            state.show = !current
        },
    },
})

export const { change } = categorySlice.actions

export default categorySlice.reducer
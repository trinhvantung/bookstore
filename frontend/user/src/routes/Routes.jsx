import React from 'react'

import { Route, Switch } from 'react-router-dom'

import Home from '../pages/Home'
import Products from '../pages/Products'
import Cart from '../pages/Cart'
import Product from '../pages/Product'
import Login from '../pages/Login'
import Order from '../pages/Order'
import Orders from '../pages/Orders'
import OrderDetail from '../pages/OrderDetail'

const Routes = () => {
    return (
        <Switch>
            <Route path='/' exact component={Home}/>
            <Route path='/product/:slug' component={Product}/>
            <Route path='/category/:slug' component={Products}/>
            <Route path='/cart' component={Cart}/>
            <Route path='/login' component={Login}/>
            <Route path='/order/:id' component={OrderDetail} />
            <Route path='/order' component={Order}/>
            <Route path='/orders' component={Orders}/>
            <Route path='/search' component={Products}/>
        </Switch>
    )
}

export default Routes

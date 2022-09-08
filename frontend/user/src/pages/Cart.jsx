import React, { useEffect, useState } from 'react'

import { Link } from 'react-router-dom'

import { Button } from 'antd'
import cartApi from '../api/cartApi'
import cartEmpty from '../assets/images/empty-cart.png'
import CartItem from '../components/CartItem'
import Helmet from '../components/Helmet'
import numberWithCommas from '../utils/numberWithCommas'

const Cart = () => {


    const [cartItems, setCartItems] = useState([])

    const [totalProducts, setTotalProducts] = useState(0)

    const [totalPrice, setTotalPrice] = useState(0)

    useEffect(() => {
        setTotalPrice(cartItems.reduce((total, item) => {
            if(item.book.discount) {
                return total + (Number(item.quantity) * Number(item.book.discountPrice))
            } else {
                return total + (Number(item.quantity) * Number(item.book.price))
            }
        }, 0))
        setTotalProducts(cartItems.length)
    }, [cartItems])

    useEffect(() => {
        cartApi.getAll().then(res => {
            console.log(res);
            setCartItems(res)
        })
    }, [])

    return (
        <Helmet title="Giỏ hàng">
            <div className="cart">
                {
                    cartItems.length === 0 ? (
                        <div className='cart_empty'>
                            <img src={cartEmpty} alt="" />
                        </div>
                    ) : (
                        <>
                            <div className="cart__info">
                                <div className="cart__info__txt">
                                    <p>
                                        Bạn đang có {totalProducts} sản phẩm trong giỏ hàng
                                    </p>
                                    <div className="cart__info__txt__price">
                                        <span>Thành tiền:</span> <span>{numberWithCommas(Number(totalPrice))} đ</span>
                                    </div>
                                </div>
                                <div className="cart__info__btn">
                                    <Link to="/order">
                                        <Button size="large" style={{ width: '100%' }} type='primary'>
                                            Đặt hàng
                                        </Button>
                                    </Link>
                                    <br />
                                    <br />
                                    <Link to="/">
                                        <Button size="large" style={{ width: '100%' }}>
                                            Tiếp tục mua hàng
                                        </Button>
                                    </Link>

                                </div>
                            </div>
                            <div className="cart__list">
                                {
                                    cartItems.map((item, index) => (
                                        <CartItem item={item} key={index} setCartItems={setCartItems} />
                                    ))
                                }
                            </div>
                        </>
                    )
                }
            </div>
        </Helmet>
    )
}

export default Cart

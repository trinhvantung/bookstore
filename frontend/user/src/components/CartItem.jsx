import PropTypes from 'prop-types'
import React, { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import cartApi from '../api/cartApi'
import numberWithCommas from '../utils/numberWithCommas'

const CartItem = props => {
    const itemRef = useRef(null)

    const [item, setItem] = useState(props.item)
    const [quantity, setQuantity] = useState(props.item.quantity)
    const isChangeQuantity = useRef(null)
    const setCartItems = props.setCartItems

    useEffect(() => {
        setItem(props.item)
        setQuantity(props.item.quantity)
    }, [props.item])

    const updateQuantity = (type) => {
        let temp = quantity
        if (type === '+') {
            setQuantity(quantity + 1)
            temp++
        } else {
            setQuantity(quantity - 1 < 1 ? 1 : quantity - 1)
            temp = temp - 1 < 1 ? 1 : temp - 1
        }

        if(isChangeQuantity.current) {
            clearTimeout(isChangeQuantity.current)
        }

        isChangeQuantity.current = setTimeout(() => {
            cartApi.update(item.id, temp).then(res => {
                console.log(res);
                setCartItems(prev => {
                    const newItems = [...prev]
                    newItems.filter(c => c.id = item.id)[0].quantity = temp

                    return newItems
                })
            })
        },500)
    }

    const removeCartItem = () => {
        cartApi.delete(item.id).then(res => {
            setCartItems(prev => {
                const newItems = prev.filter(c => c.id !== item.id)

                return newItems
            })
        })
    }

    return (
        <div className="cart__item" ref={itemRef}>
            <div className="cart__item__image">
                <img src={item.book.thumbnailUrl} alt="" />
            </div>
            <div className="cart__item__info">
                <div className="cart__item__info__name">
                    <Link to={`/catalog/${item.book.slug}`}>
                        {`${item.book.name}`}
                    </Link>
                </div>
                {
                    item.book.discount ? (
                        <div style={{
                            display: 'flex',
                            alignItems: 'center',
                            flexDirection: 'column'
                        }}>
                            <div className="cart__item__info__price">
                                {numberWithCommas(item.book.discountPrice)} đ
                            </div>
                            <div className="cart__item__info__price"
                                style={{
                                    fontSize: 14,
                                    color: '#b8b8b8',
                                    textDecoration: 'line-through',
                                    // marginLeft: 8
                                }}>
                                {numberWithCommas(item.book.price)} đ
                            </div>
                        </div>
                    ) : (
                        <div className="cart__item__info__price">
                            {numberWithCommas(item.book.price)} đ
                        </div>
                    )
                }
                <div className="cart__item__info__quantity">
                    <div className="cart__info__item__quantity">
                        <div className="cart__info__item__quantity__btn" onClick={() => updateQuantity('-')}>
                            <i className="bx bx-minus"></i>
                        </div>
                        <div className="cart__info__item__quantity__input">
                            {quantity}
                        </div>
                        <div className="cart__info__item__quantity__btn" onClick={() => updateQuantity('+')}>
                            <i className="bx bx-plus"></i>
                        </div>
                    </div>
                </div>
                <div className="cart__item__del">
                    <i className='bx bx-trash' onClick={() => removeCartItem()}></i>
                </div>
            </div>
        </div>
    )
}

CartItem.propTypes = {
    item: PropTypes.object
}

export default CartItem

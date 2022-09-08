import React from 'react'
import { Link } from 'react-router-dom'
import numberWithCommas from '../utils/numberWithCommas'

const OrderProductItem = (props) => {
    const { data } = props
    console.log(data);
    return (
        <div className="order-product-item">
            <div className="order-product-item__image">
                <img src={data.book.thumbnailUrl} alt={"Book Store"} />
            </div>
            <div className="order-product-item__info">
                <div className="order-product-item__info__name">
                    <Link to={`/product/${data.book.slug}`}>{data.book.name}</Link>
                </div>
                <div className="order-product-item__info__details">
                    <div className="order-product-item__info__price">
                        {numberWithCommas(data.book.discount ? data.book.discountPrice : data.book.price)}
                    </div>x
                    <div className="order-product-item__info__quantity">{data.quantity}</div>=
                    <div className="order-product-item__info__total-price">
                        {numberWithCommas(data.book.discount ? data.book.discountPrice : data.book.price * data.quantity)} đ
                    </div>
                </div>
            </div>
        </div>
    )
}

export default OrderProductItem

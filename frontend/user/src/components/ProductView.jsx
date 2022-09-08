import PropTypes from 'prop-types'
import React, { useEffect, useState } from 'react'

import { withRouter } from 'react-router'



import { Button, notification } from 'antd'
import cartApi from '../api/cartApi'
import numberWithCommas from '../utils/numberWithCommas'

const ProductView = props => {
    let product = props.product

    const [previewImg, setPreviewImg] = useState(product.images[0].pathUrl)

    const [descriptionExpand, setDescriptionExpand] = useState(false)

    const [quantity, setQuantity] = useState(1)

    const updateQuantity = (type) => {
        if (type === 'plus') {
            setQuantity(quantity + 1)
        } else {
            setQuantity(quantity - 1 < 1 ? 1 : quantity - 1)
        }
    }

    useEffect(() => {
        setPreviewImg(product.images[0].pathUrl)
        setQuantity(1)
    }, [product])

    const addToCart = () => {
        cartApi.add(product.id, quantity).then(res => {
            console.log(res);
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
        })
    }

    return (
        <div className="product">
            <div className="product__images">
                <div className="product__images__list">
                    {
                        product.images.map(item => (
                            <div key={item.id} className="product__images__list__item"
                                onClick={() => setPreviewImg(item.pathUrl)}>
                                <img src={item.pathUrl} alt="" />
                            </div>
                        ))
                    }
                </div>
                <div className="product__images__main">
                    <img src={previewImg} alt="" />
                </div>
                <div className={`product-description ${descriptionExpand ? 'expand' : ''}`}>
                    <div className="product-description__title">
                        Chi tiết sản phẩm
                    </div>
                    <div className="product-description__content" dangerouslySetInnerHTML={{ __html: product.description }}></div>
                    <div className="product-description__toggle">
                        <Button size="sm" onClick={() => setDescriptionExpand(!descriptionExpand)}>
                            {
                                descriptionExpand ? 'Thu gọn' : 'Xem thêm'
                            }
                        </Button>
                    </div>
                </div>
            </div>
            <div className="product__info">
                <h1 className="product__info__title">{product.name}</h1>
                <div className="product__info__item">
                    {
                        product.discount && (
                            <div className="product__info__item__price">
                                {numberWithCommas(product.discountPrice)}
                                <span className="product-card__price__old">
                                    <del>{numberWithCommas(product.price)}</del>
                                </span>
                            </div>
                        )
                    }
                    {
                        !product.discount && (
                            <span className="product__info__item__price">
                                {numberWithCommas(product.price || 0)} đ
                            </span>
                        )
                    }
                </div>
                <div className="product__info__item">
                    <div className="product__info__item__title">
                        Số lượng
                    </div>
                    <div className="product__info__item__quantity">
                        <div className="product__info__item__quantity__btn" onClick={() => updateQuantity('minus')}>
                            <i className="bx bx-minus"></i>
                        </div>
                        <div className="product__info__item__quantity__input">
                            {quantity}
                        </div>
                        <div className="product__info__item__quantity__btn" onClick={() => updateQuantity('plus')}>
                            <i className="bx bx-plus"></i>
                        </div>
                    </div>
                </div>
                <div className="product__info__item">
                    <Button size='large' type='primary' onClick={() => addToCart()}>Thêm vào giỏ hàng</Button>
                </div>
            </div>
            <div className={`product-description mobile ${descriptionExpand ? 'expand' : ''}`}>
                <div className="product-description__title">
                    Chi tiết sản phẩm
                </div>
                <div className="product-description__content" dangerouslySetInnerHTML={{ __html: product.description }}></div>
                <div className="product-description__toggle">
                    <Button size="sm" onClick={() => setDescriptionExpand(!descriptionExpand)}>
                        {
                            descriptionExpand ? 'Thu gọn' : 'Xem thêm'
                        }
                    </Button>
                </div>
            </div>
        </div>
    )
}

ProductView.propTypes = {
    product: PropTypes.object
}

export default withRouter(ProductView)

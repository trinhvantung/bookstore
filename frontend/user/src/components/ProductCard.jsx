import PropTypes from 'prop-types'
import React from 'react'

import { Link } from 'react-router-dom'


import numberWithCommas from '../utils/numberWithCommas'

const ProductCard = props => {
    const product = props.product

    return (
        <div className="product-card">
            <Link to={`/product/${product.slug}`}>
                <div className="product-card__image">
                    <img src={product.thumbnailUrl} alt="" />
                </div>
                <h3 className="product-card__name">{product.name}</h3>

                {
                    product.discount && (
                        <div className="product-card__price">
                            {numberWithCommas(product.discountPrice)}
                            <span className="product-card__price__old">
                                <del>{numberWithCommas(product.price)}</del>
                            </span>
                        </div>
                    )
                }
                {
                    !product.discount && (
                        <div className="product-card__price">
                            {numberWithCommas(product.price)}
                        </div>
                    )
                }
            </Link>
        </div>
    )
}

ProductCard.propTypes = {
    // img01: PropTypes.string.isRequired,
    // name: PropTypes.string.isRequired,
    // price: PropTypes.number.isRequired,
    // slug: PropTypes.string.isRequired,
    product: PropTypes.object.isRequired
}

export default ProductCard

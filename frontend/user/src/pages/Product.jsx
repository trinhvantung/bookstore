import React, { useEffect } from 'react'

import Helmet from '../components/Helmet'
import ProductView from '../components/ProductView'
import Section, { SectionBody } from '../components/Section'

import productApi from '../api/productApi'
import { useState } from 'react'

const Product = props => {

    const slug = props.match.params.slug

    const [product, setProduct] = useState({})

    useEffect(() => {
        window.scrollTo(0,0)

        productApi.getBySlug(slug).then(res => {
            setProduct(res)
        })
    }, [slug])

    return (
        <Helmet title={product.name || ""}>
            <Section>
                <SectionBody>
                    {
                        product.name && <ProductView product={product}/>
                    }
                </SectionBody>
            </Section>
        </Helmet>
    )
}

export default Product

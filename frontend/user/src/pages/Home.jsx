import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import productApi from '../api/productApi'

import Grid from '../components/Grid'
import Helmet from '../components/Helmet'
import PolicyCard from '../components/PolicyCard'
import ProductCard from '../components/ProductCard'
import Section, { SectionBody } from '../components/Section'

const policy = [
    {
        name: "Miễn phí giao hàng",
        description: "Miễn phí ship với đơn hàng > 239K",
        icon: "bx bx-shopping-bag"
    },
    {
        name: "Thanh toán COD",
        description: "Thanh toán khi nhận hàng (COD)",
        icon: "bx bx-credit-card"
    },
    {
        name: "Khách hàng VIP",
        description: "Ưu đãi dành cho khách hàng VIP",
        icon: "bx bx-diamond"
    },
    {
        name: "Hỗ trợ bảo hành",
        description: "Đổi, sửa đồ tại tất cả store",
        icon: "bx bx-donate-heart"
    }
]

const Home = () => {
    const [products, setProducts] = useState([])

    useEffect(() => {
        productApi.getLatest().then(res => {
            setProducts(res)
        })
    }, [])

    return (
        <Helmet title="Trang chủ">
            {/* new arrival section */}
            <Section>
                <SectionBody>
                    <Grid
                        col={4}
                        mdCol={2}
                        smCol={2}
                        gap={20}
                    >
                        {
                            products.map((item, index) => (
                                <ProductCard
                                    key={index}
                                    product={item}
                                />
                            ))
                        }
                    </Grid>
                </SectionBody>
            </Section>
            {/* end new arrival section */}
            
           

            
            {/* policy section */}
            <Section>
                <SectionBody>
                    <Grid
                        col={4}
                        mdCol={2}
                        smCol={1}
                        gap={20}
                    >
                        {
                            policy.map((item, index) => <Link key={index} to="/policy">
                                <PolicyCard
                                    name={item.name}
                                    description={item.description}
                                    icon={item.icon}
                                />
                            </Link>)
                        }
                    </Grid>
                </SectionBody>
            </Section>
            {/* end policy section */}
        </Helmet>
    )
}

export default Home

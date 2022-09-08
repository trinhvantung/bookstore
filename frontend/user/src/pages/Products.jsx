import React, { useEffect, useRef, useState } from 'react'
import queryString from 'query-string';
import Helmet from '../components/Helmet'
import { Checkbox, Pagination, Radio, Spin, Button } from 'antd'
import { useLocation } from 'react-router-dom'
import productApi from '../api/productApi'
import authorApi from '../api/authorApi'
import publisherApi from '../api/publisherApi'
import Grid from '../components/Grid'
import ProductCard from '../components/ProductCard'

const sort = [
    {
        display: "Mới nhất",
        sort: "1"
    },
    {
        display: "Cũ nhất",
        sort: "2"
    },
    {
        display: "Giá tăng dần",
        sort: "3"
    },
    {
        display: "Giá giảm dần",
        sort: "4"
    }
]

const filterPrices = [
    {
        display: "Dưới 20.000",
        price: "0-20000"
    },
    {
        display: "Từ 20.000-50.000",
        price: "20000-50000"
    },
    {
        display: "Từ 50.000-100.000",
        price: "50000-100000"
    },
    {
        display: "Từ 100.000-150.000",
        price: "100000-150000"
    },
    {
        display: "Trên 150.000",
        price: "150000-50000000"
    }
]

const Catalog = (props) => {
    const initFilter = {
        prices: [],
        authors: [],
        publishers: [],
        sort: "1"
    }
    const location = useLocation()

    const [products, setProducts] = useState([])
    const [pageProduct, setPageProduct] = useState({})

    const [authors, setAuthors] = useState([])
    const [publishers, setPublishers] = useState([])

    const [isLoading, setIsLoading] = useState(false)

    const [filter, setFilter] = useState(initFilter)


    const clearFilter = () => setFilter(initFilter)

    const filterRef = useRef(null)

    const showHideFilter = () => filterRef.current.classList.toggle('active')

    const getTitle = () => {
        if (location.pathname === '/search') {
            return "Kết quả tìm kiếm cho: " + queryString.parse(location.search).q
        } else {
            return "Sản phẩm"
        }
    }

    useEffect(() => {
        authorApi.getAll().then(res => {
            setAuthors(res)
        })
        publisherApi.getAll().then(res => {
            setPublishers(res)
        })
    }, [location.pathname])


    const changePage = page => {
        if (location.pathname.startsWith('/category')) {
            loadByCategory(filter, page)
        } else if (location.pathname === '/search') {
            search(filter, page)
        }
    }


    const loadByCategory = (filter, page) => {
        setIsLoading(true)
        window.scrollTo(0, 0)
        const slug = props.match.params.slug
        productApi.getByCategory(slug, { ...filter, page }).then(res => {
            setProducts(res.content)
            setPageProduct(res)
            console.log(res);
            setIsLoading(false)
        })
    }

    const search = (filter, page) => {
        setIsLoading(true)
        window.scrollTo(0, 0)
        const q = queryString.parse(location.search).q
        productApi.search(q, { ...filter, page }).then(res => {
            setProducts(res.content)
            setPageProduct(res)
            setIsLoading(false)
        })

    }

    const changeFilter = (key, value) => {
        setFilter(prev => {
            const newValue = { ...prev }
            newValue[key] = value
            return newValue
        })
    }

    useEffect(() => {
        if (location.pathname.startsWith('/category')) {
            loadByCategory(filter, 1)
        } else if (location.pathname === '/search') {
            search(filter, 1)
        }
    }, [filter, queryString.parse(location.search).q])


    return (
        <Helmet title={getTitle()}>
            <div className="catalog">
                <div className="catalog__filter" ref={filterRef}>
                    <div className="catalog__filter__close" onClick={() => showHideFilter()}>
                        <i className="bx bx-left-arrow-alt"></i>
                    </div>
                    <div className="catalog__filter__widget">
                        <div className="catalog__filter__widget__title">
                            Sắp xếp theo
                        </div>
                        <div className="catalog__filter__widget__content">
                            <Radio.Group defaultValue={"1"}
                                onChange={data => changeFilter("sort", data.target.value)}
                                value={filter.sort}>
                                {
                                    sort.map((item, index) => (
                                        <div key={index} className="catalog__filter__widget__content__item">
                                            <Radio value={item.sort}>{item.display}</Radio>
                                        </div>
                                    ))
                                }
                            </Radio.Group>
                        </div>
                    </div>
                    <div className="catalog__filter__widget">
                        <div className="catalog__filter__widget__title">
                            Chọn khoảng giá
                        </div>
                        <div className="catalog__filter__widget__content">

                            <Checkbox.Group onChange={data => changeFilter("prices", data)}
                                value={filter.prices}>
                                {
                                    filterPrices.map((item, index) => (
                                        <div key={index} className="catalog__filter__widget__content__item">
                                            <Checkbox value={item.price}>{item.display}</Checkbox>
                                        </div>
                                    ))
                                }
                            </Checkbox.Group>
                        </div>
                    </div>
                    <div className="catalog__filter__widget">
                        <div className="catalog__filter__widget__title">
                            Chọn tác giả
                        </div>
                        <div className="catalog__filter__widget__content">
                            <Checkbox.Group onChange={data => changeFilter("authors", data)}
                                value={filter.authors}>
                                {
                                    authors.map((item, index) => (
                                        <div key={index} className="catalog__filter__widget__content__item">
                                            <Checkbox value={item.id}>{item.name}</Checkbox>
                                        </div>
                                    ))
                                }
                            </Checkbox.Group>
                        </div>
                    </div>
                    <div className="catalog__filter__widget">
                        <div className="catalog__filter__widget__title">
                            Chọn nhà xuất bản
                        </div>
                        <div className="catalog__filter__widget__content">

                            <Checkbox.Group onChange={data => changeFilter("publishers", data)}
                                value={filter.publishers}>
                                {
                                    publishers.map((item, index) => (
                                        <div key={index} className="catalog__filter__widget__content__item">
                                            <Checkbox value={item.id}>{item.name}</Checkbox>
                                        </div>
                                    ))
                                }
                            </Checkbox.Group>
                        </div>
                    </div>

                    <div className="catalog__filter__widget">
                        <div className="catalog__filter__widget__content">
                            <Button type='primary' onClick={clearFilter}
                                disabled={filter.authors.length === 0
                                    && filter.prices.length === 0
                                    && filter.publishers.length === 0
                                    && filter.sort === "1"}>xóa bộ lọc</Button>
                        </div>
                    </div>
                </div>
                <div className="catalog__filter__toggle">
                    <Button onClick={() => showHideFilter()}>bộ lọc</Button>
                </div>
                <Spin tip="Loading..." spinning={isLoading}>
                    <div className="catalog__content">
                        {
                            location.pathname === '/search' && (
                                <h2 style={{ marginBottom: 16 }}>Kết quả tìm kiếm cho {queryString.parse(location.search).q}</h2>
                            )
                        }

                        <Grid
                            col={3}
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
                        {
                            pageProduct.totalPages > 1 && (
                                <div style={{
                                    display: 'flex',
                                    justifyContent: 'center',
                                    width: '100%'
                                }}>
                                    <Pagination current={pageProduct.number + 1}
                                        defaultCurrent={pageProduct.number + 1} onChange={changePage}
                                        total={pageProduct.totalElements} defaultPageSize={pageProduct.size} />
                                </div>
                            )
                        }
                    </div>
                </Spin>
            </div>
        </Helmet>
    )
}

export default Catalog

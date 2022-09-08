import React from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Link } from 'react-router-dom'
import { change } from '../redux/category'
import Grid from '../components/Grid'
import { useEffect } from 'react'
import categoryApi from '../api/categoryApi'
import { useState } from 'react'



export const Category = () => {
    const show = useSelector((state) => state.category.show)
    const dispatch = useDispatch()
    const [categories, setCategories] = useState([])

    if (show) {
        document.body.style.overflow = "hidden"
    } else {
        document.body.style.overflow = "auto"
    }

    useEffect(() => {
        categoryApi.getAll().then(res => {
            setCategories(res)
        })
    }, [])

    return (
        (
            <div className={`category container ${show ? 'active' : ''}`}>
                <div className="category_header">
                    <h2>
                        Tất cả danh mục
                    </h2>
                    <div onClick={() => dispatch(change())}>Đóng</div>
                </div>
                <div className="category_content container">
                    <Grid
                        col={6}
                        mdCol={2}
                        smCol={1}
                        gap={20}
                    >
                        {
                            categories.filter(c => c.parent == null)
                                .map(category => (
                                    <div key={category.id} className="category_block">
                                        <div className="category_parent">
                                            <div onClick={() => dispatch(change())}>
                                                <Link to={`/category/${category.slug}`}>{category.name}</Link>
                                            </div>
                                        </div>
                                        <ul className='category_children_list'>
                                            {
                                                categories.filter(c => c.parent && c.parent.id === category.id)
                                                    .map(item => (
                                                        <li key={item.id} onClick={() => dispatch(change())}>
                                                            <Link to={`/category/${item.slug}`}>{item.name}</Link>
                                                        </li>
                                                    ))
                                            }
                                        </ul>
                                    </div>
                                ))
                        }
                    </Grid>
                </div>
            </div>
        )
    )
}

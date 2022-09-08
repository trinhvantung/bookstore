import React, { useRef } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Link, useHistory, useLocation } from 'react-router-dom'

import { change } from '../redux/category'

import { Badge, Form, Input, Modal, Popover } from 'antd'
import { useState } from 'react'
import logo from '../assets/images/logo.png'

const mainNav = [
    {
        display: "Trang chủ",
        path: "/"
    },
    {
        display: "Danh mục"
    },
    {
        display: "Đơn hàng",
        path: "/orders"
    }
]



const Header = () => {
    const dispatch = useDispatch()
    const { pathname } = useLocation()
    const history = useHistory()

    const activeNav = mainNav.findIndex(e => e.path === pathname)
    const [searchValue, setSearchValue] = useState('')

    const [showSearch, setShowSearch] = useState(false)

    const headerRef = useRef(null)

    const menuLeft = useRef(null)

    const menuToggle = () => menuLeft.current.classList.toggle('active')
    const cartItems = useSelector((state) => state.cartItems.value)

    const [isModalVisible, setIsModalVisible] = useState(false);
    const [form] = Form.useForm();

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        form.submit()
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const search = (e) => {
        e.preventDefault()
        history.push(`/search?q=${searchValue}`)
    }

    const logout = () => {
        localStorage.clear("accessToken")
        window.location.href = "/"
    }
    const content = (
        <div>
            <div style={{ padding: "0 0 5px 0" }} onClick={showModal}>
                <div style={{
                    cursor: 'pointer'
                }}>
                    Đổi mật khẩu
                </div>
            </div>
            <div style={{ padding: "5px 0 0 0" }}>
                <div style={{
                    cursor: 'pointer'
                }} onClick={logout}>
                    Đăng xuất
                </div>
            </div>
        </div>
    );


    return (
        <div className="header" ref={headerRef}>
            <div className="container">
                <div className="header__logo">
                    <Link to="/">
                        <img src={logo} alt="" />
                    </Link>
                </div>
                <div className="header__menu">
                    <div className="header__menu__mobile-toggle" onClick={menuToggle}>
                        <i className='bx bx-menu'></i>
                    </div>
                    <div className="header__menu__left" ref={menuLeft}>
                        <div className="header__menu__left__close" onClick={menuToggle}>
                            <i className='bx bx-chevron-left'></i>
                        </div>
                        {
                            mainNav.map((item, index) => (
                                <div
                                    key={index}
                                    className={`header__menu__item header__menu__left__item ${index === activeNav ? 'active' : ''}`}
                                    onClick={menuToggle}
                                >
                                    {
                                        item.path ? (
                                            <Link to={item.path}>
                                                <span>{item.display}</span>
                                            </Link>
                                        ) : (
                                            <span onClick={() => dispatch(change())} style={{ cursor: 'pointer' }}>{item.display}</span>
                                        )
                                    }
                                </div>
                            ))
                        }
                    </div>
                    <div className="header__menu__right">
                        <div className="header__menu__item header__menu__right__item"
                            onClick={() => setShowSearch(true)}>
                            <i className="bx bx-search"></i>
                        </div>
                        {
                            localStorage.getItem("accessToken") && (
                                <div className="header__menu__item header__menu__right__item">
                                    <Badge count={cartItems.length}>
                                        <Link to="/cart">
                                            <i className="bx bx-shopping-bag"></i>
                                        </Link>
                                    </Badge>
                                </div>
                            )
                        }
                        <div className="header__menu__item header__menu__right__item">

                            {
                                localStorage.getItem("accessToken") ? (
                                    <Popover content={content}
                                        title={<div style={{ padding: "5px 0" }}>Tài khoản</div>}
                                        trigger="hover" showArrow={false}>
                                        <i className="bx bx-user"></i>
                                    </Popover>
                                ) : (
                                    <Link to="/login" style={{ height: 26 }}>
                                        <i className="bx bx-user"></i>
                                    </Link>
                                )
                            }
                        </div>
                    </div>
                </div>
                <div className={`search ${showSearch ? 'active' : ''}`}>
                    <form className="search_form" onSubmit={search}>
                        <input type="text" placeholder='Tìm kiếm' value={searchValue} onChange={e => setSearchValue(e.target.value)} />
                        <button disabled={searchValue.length <= 1} type='submit'><i className='bx bx-search'></i></button>
                    </form>
                    <div className="search_close" onClick={() => setShowSearch(false)}>
                        <i className='bx bx-x-circle'></i>
                    </div>
                </div>
            </div>




            <Modal title="Đổi mật khẩu" visible={isModalVisible}
                onOk={handleOk}
                onCancel={handleCancel}
                cancelText="Đóng"
                okText="Đổi mật khẩu">
                <Form
                    form={form}
                    layout="vertical"
                    validateTrigger="onSubmit"
                    requiredMark={false}
                >
                    <Form.Item label="Mật khẩu" name='password'
                        rules={[
                            {
                                required: true,
                                message: "Mật khẩu không được để trống"
                            },
                            {
                                validator: async (_, value) => {
                                    if (value && (value.length < 5 || value.length > 30)) {
                                        return Promise.reject('Mật khẩu có độ dài từ 5 đến 30 ký tự');
                                    }
                                    return Promise.resolve()
                                }
                            }
                        ]}>
                        <Input.Password placeholder="Mật khẩu" size="large" type={'password'} />
                    </Form.Item>
                    <Form.Item
                        label="Mật khẩu mới" name='newPassword'
                        rules={[
                            {
                                required: true,
                                message: "Mật khẩu không được để trống"
                            },
                            {
                                validator: async (_, value) => {
                                    if (value && (value.length < 5 || value.length > 30)) {
                                        return Promise.reject('Mật khẩu có độ dài từ 5 đến 30 ký tự');
                                    }
                                    return Promise.resolve()
                                }
                            }
                        ]}
                    >
                        <Input.Password placeholder="Mật khẩu mới" type="password" size="large" />
                    </Form.Item>
                    {/* <Form.Item>
                        <div className="login__button">
                            <Button type="primary" size="large">Đổi mật khẩu</Button>
                        </div>
                    </Form.Item> */}
                </Form>
            </Modal>
        </div>
    )
}

export default Header

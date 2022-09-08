import { cilPen, cilTrash } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import { Avatar, notification, Popconfirm, Space, Switch, Table } from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import productApi from '../../api/productApi';



const Products = () => {

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Image',
            dataIndex: 'thumbnailUrl',
            key: 'thumbnailUrl',
            render: (value) => <Avatar shape="square" size={64} src={value} />
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Slug',
            dataIndex: 'slug',
            key: 'slug',
        },
        {
            title: 'In Stock',
            dataIndex: 'inStock',
            key: 'inStock',
            render: (value) => <Switch checked={value} />
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (value) => <Switch checked={value} />
        },
        {
            title: '',
            dataIndex: 'id',
            key: 'id',
            render: (id) => (
                <Space size={'large'}>
                    <Link to={"/product/update?id=" + id}>
                        <CIcon icon={cilPen} style={{ fontSize: 20 }} />
                    </Link>
                    <div style={{ cursor: 'pointer' }}>
                        <Popconfirm placement="top" title={`Delete product with id: ${id}`} onConfirm={() => onDelete(id)} okText="Yes" cancelText="No">
                            <CIcon icon={cilTrash} style={{ fontSize: 20 }} />
                        </Popconfirm>
                    </div>
                </Space>
            )
        }
    ];

    const [products, setProducts] = useState([])
    const [isLoading, setIsLoading] = useState(false);
    const [response, setResponse] = useState({})

    useEffect(() => {
        load(1)
    }, [])

    const changePage = page => {
        load(page.current)
    }

    const onDelete = id => {
        productApi.delete(id).then(res => {
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
            setProducts(prev => prev.filter(product => product.id !== id))

        }).catch(err => {
        })
    }

    const load = page => {
        setIsLoading(true)
        productApi.getAllByPage({ page }).then(res => {
            setProducts(res.content)
            setIsLoading(false)
            setResponse(res)
        })
    }

    return (
        <div>
            <Table rowKey={'id'} dataSource={products} columns={columns} loading={isLoading}
                pagination={{ total: response.totalElements, showSizeChanger: false }}
                onChange={changePage} />;
        </div>
    )
}

export default Products
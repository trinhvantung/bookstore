import { cilPen, cilTrash } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import { notification, Popconfirm, Space, Table } from 'antd';
import React from 'react';
import { useState } from 'react';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import authorApi from '../../api/authorApi';



const Authors = () => {
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '',
            dataIndex: 'id',
            key: 'id',
            render: (id) => (
                <Space size={'large'}>
                    <Link to={"/author/update?id=" + id}>
                        <CIcon icon={cilPen} style={{ fontSize: 20 }} />
                    </Link>
                    <div style={{ cursor: 'pointer' }}>
                        <Popconfirm placement="top" title={`Delete author with id: ${id}`} onConfirm={() => onDelete(id)} okText="Yes" cancelText="No">
                            <CIcon icon={cilTrash} style={{ fontSize: 20 }} />
                        </Popconfirm>
                    </div>
                </Space>
            )
        }
    ];

    const [authors, setAuthors] = useState([])
    const [isLoading, setIsLoading] = useState(false);
    const [response, setResponse] = useState({})

    useEffect(() => {
        load(1)
    }, [])

    const changePage = page => {
        load(page.current)
    }

    const onDelete = id => {
        authorApi.delete(id).then(res => {
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
            setAuthors(prev => prev.filter(author => author.id !== id))

        }).catch(err => {
        })
    }

    const load = page => {
        console.log(page)
        setIsLoading(true)
        authorApi.getAllByPage({ page }).then(res => {
            setAuthors(res.content)
            setIsLoading(false)
            setResponse(res)
        })
    }
    return (
        <div>
            <Table rowKey={'id'} dataSource={authors} columns={columns} loading={isLoading}
                pagination={{ total: response.totalElements, showSizeChanger: false }}
                onChange={changePage} />;
        </div>
    )
}

export default Authors
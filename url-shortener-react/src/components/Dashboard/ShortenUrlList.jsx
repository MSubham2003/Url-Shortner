import React from 'react'
import PropTypes from 'prop-types'
import ShortenItem from './ShortenItem'

const ShortenUrlList = ({ data }) => {
  return (
    <div className='my-6 space-y-4'>
        {data.map((item) => (
            <ShortenItem key={item.id} {...item} />
        ))}
    </div>
  )
}
ShortenUrlList.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    })
  ).isRequired,
}

// export default ShortenUrlList
export default ShortenUrlList
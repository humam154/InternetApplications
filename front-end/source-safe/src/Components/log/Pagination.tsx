import React, {  } from "react";
import PropTypes from "prop-types";

import styles from "./LogsPage.module.css";

 interface Props {
    currentPage: number;
    totalPages: number;
    handleNextPage: (page: number) => void;
    handlePrevPage: (page: number) => void;
  }
const Pagination = (props: Props) => {

    const { currentPage, totalPages, handlePrevPage, handleNextPage } = props;

  return (
    <div className={styles.paginationWrapper}>
      <button
        className={styles.paginationButton}
        onClick={() => handlePrevPage(currentPage)}
        disabled={currentPage === 1}
      >
        &larr;
      </button>

      <span className={styles.paginationInfo}>
        Page {currentPage} of {totalPages}
      </span>

      <button
        className={styles.paginationButton}
        onClick={() => handleNextPage(currentPage)}
        disabled={currentPage === totalPages}
      >
       &rarr;
      </button>
    </div>
  );
};

Pagination.propTypes = {
    currentPage: PropTypes.number.isRequired,
    totalPages: PropTypes.number.isRequired,
    handlePrevPage: PropTypes.func.isRequired,
    handleNextPage: PropTypes.func.isRequired,
  };

export default Pagination
